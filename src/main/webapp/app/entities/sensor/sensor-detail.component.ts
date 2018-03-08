import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager } from 'ng-jhipster';

import { Sensor } from './sensor.model';
import { SensorService } from './sensor.service';

@Component({
    selector: 'jhi-sensor-detail',
    templateUrl: './sensor-detail.component.html'
})
export class SensorDetailComponent implements OnInit, OnDestroy {

    sensor: Sensor;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private sensorService: SensorService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInSensors();
    }

    load(id) {
        this.sensorService.find(id).subscribe((sensor) => {
            this.sensor = sensor;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInSensors() {
        this.eventSubscriber = this.eventManager.subscribe(
            'sensorListModification',
            (response) => this.load(this.sensor.id)
        );
    }
}
