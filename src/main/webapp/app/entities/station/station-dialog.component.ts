import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Station } from './station.model';
import { StationPopupService } from './station-popup.service';
import { StationService } from './station.service';
import { Sensor, SensorService } from '../sensor';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-station-dialog',
    templateUrl: './station-dialog.component.html'
})
export class StationDialogComponent implements OnInit {

    station: Station;
    isSaving: boolean;

    sensors: Sensor[];

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private stationService: StationService,
        private sensorService: SensorService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.sensorService.query()
            .subscribe((res: ResponseWrapper) => { this.sensors = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.station.id !== undefined) {
            this.subscribeToSaveResponse(
                this.stationService.update(this.station));
        } else {
            this.subscribeToSaveResponse(
                this.stationService.create(this.station));
        }
    }

    private subscribeToSaveResponse(result: Observable<Station>) {
        result.subscribe((res: Station) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: Station) {
        this.eventManager.broadcast({ name: 'stationListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }

    trackSensorById(index: number, item: Sensor) {
        return item.id;
    }

    getSelected(selectedVals: Array<any>, option: any) {
        if (selectedVals) {
            for (let i = 0; i < selectedVals.length; i++) {
                if (option.id === selectedVals[i].id) {
                    return selectedVals[i];
                }
            }
        }
        return option;
    }
}

@Component({
    selector: 'jhi-station-popup',
    template: ''
})
export class StationPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private stationPopupService: StationPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.stationPopupService
                    .open(StationDialogComponent as Component, params['id']);
            } else {
                this.stationPopupService
                    .open(StationDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
