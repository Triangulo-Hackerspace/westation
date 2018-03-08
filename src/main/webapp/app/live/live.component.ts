import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Account, ResponseWrapper, Principal } from '../shared';

import { MqttService, MqttMessage } from 'ngx-mqtt';
import { Subscription } from 'rxjs/Subscription';
import { Observable } from 'rxjs/Observable';


import { StationService } from '../entities/station/station.service';
import { SensorService } from '../entities/sensor/sensor.service';
import { Station } from '../entities/station/station.model';
import { Sensor } from '../entities/sensor/sensor.model';

export type QoS = 0 | 1 | 2;


@Component({
    selector: 'jhi-live',
    templateUrl: './live.component.html',
    styleUrls: [
        'live.css'
    ]

})
export class LiveComponent implements OnInit {
    public topic: string;
    public retain: boolean;
    public qos: QoS = 0;
    public filter: string;
    public message: string;
    account: Account;
    public stations: Station[];
    public sensors: Sensor[];
    public sensor: Sensor;

    constructor(
        private principal: Principal,
        private eventManager: JhiEventManager,
        private mqtt: MqttService,
        private cdRef: ChangeDetectorRef,
        private stationService: StationService,
        private sensorService: SensorService,
        private jhiAlertService: JhiAlertService,
    ) {
        mqtt.onConnect.subscribe((e) => console.log('onConnect', e));
        mqtt.onError.subscribe((e) => console.log('onError', e));
        mqtt.onClose.subscribe(() => console.log('onClose'));
        mqtt.onReconnect.subscribe(() => console.log('onReconnect'));
        mqtt.onMessage.subscribe((e) => console.log('onMessage', e));
    }

    public get state() {
        return this.mqtt.state;
    }

    public get observables() {
        return this.mqtt.observables;
    }

    public publish(topic: string, message: string, retain = false, qos: QoS = 0): void {
        this.mqtt
            .publish(topic, message, { retain, qos })
            .subscribe((err) => console.log(err));
    }

    public subscribe(filter: string): void {
        this.mqtt.observe(filter);
    }

    public unsubscribe(filter: string): void {
        this.mqtt.observables[filter] = null;
    }

    ngOnInit() {

        this.principal.identity().then((account) => {
            this.account = account;
        });
        this.registerAuthenticationSuccess();

        if(this.isAuthenticated()){

            this.sensorService.findAll().subscribe(
                (res: ResponseWrapper) => {
                    this.sensors = res.json;
                    this.stationService.findAll().subscribe(
                        (res: ResponseWrapper) => {
                            this.stations = res.json;
                            this.subcribeAll();
                        },
                        (res: ResponseWrapper) => this.onError(res.json)
                    );
                },
                (res: ResponseWrapper) => this.onError(res.json)
            );
        }
    }

    getSensorName(sensorId: number): string {
        for (let sen of this.sensors) {
            if(sensorId === sen.id){ return sen.name}
        }
        return 'Not Found!';
    }

    getSensorTopic(sensorId: number): string {
        for (let sen of this.sensors) {
            if(sensorId === sen.id){ return sen.topic}
        }
        return '';
    }

    subcribeAll(){
        for (let station of this.stations) {
            //console.log(entry); // 1, "string", false
            for (let sensor of station.sensors) {
                this.sensorService.find(sensor.id).subscribe(
                    (res: Sensor) => {
                        this.sensor = res;
                        this.subscribe(station.sufix+"/"+this.sensor.topic);
                    },
                    (res: ResponseWrapper) => this.onError(res.json));
            }
        }
    }

    isAuthenticated() {
        return this.principal.isAuthenticated();
    }

    registerAuthenticationSuccess() {
        this.eventManager.subscribe('authenticationSuccess', (message) => {
            this.principal.identity().then((account) => {
                this.account = account;
            });
        });
    }

    private onError(error) {
        this.jhiAlertService.error(error.message, null, null);
    }


}
