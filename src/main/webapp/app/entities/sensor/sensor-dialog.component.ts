import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Sensor } from './sensor.model';
import { SensorPopupService } from './sensor-popup.service';
import { SensorService } from './sensor.service';

@Component({
    selector: 'jhi-sensor-dialog',
    templateUrl: './sensor-dialog.component.html'
})
export class SensorDialogComponent implements OnInit {

    sensor: Sensor;
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private sensorService: SensorService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.sensor.id !== undefined) {
            this.subscribeToSaveResponse(
                this.sensorService.update(this.sensor));
        } else {
            this.subscribeToSaveResponse(
                this.sensorService.create(this.sensor));
        }
    }

    private subscribeToSaveResponse(result: Observable<Sensor>) {
        result.subscribe((res: Sensor) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: Sensor) {
        this.eventManager.broadcast({ name: 'sensorListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }
}

@Component({
    selector: 'jhi-sensor-popup',
    template: ''
})
export class SensorPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private sensorPopupService: SensorPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.sensorPopupService
                    .open(SensorDialogComponent as Component, params['id']);
            } else {
                this.sensorPopupService
                    .open(SensorDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
