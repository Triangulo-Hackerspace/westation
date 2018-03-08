import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Sensor } from './sensor.model';
import { SensorPopupService } from './sensor-popup.service';
import { SensorService } from './sensor.service';

@Component({
    selector: 'jhi-sensor-delete-dialog',
    templateUrl: './sensor-delete-dialog.component.html'
})
export class SensorDeleteDialogComponent {

    sensor: Sensor;

    constructor(
        private sensorService: SensorService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.sensorService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'sensorListModification',
                content: 'Deleted an sensor'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-sensor-delete-popup',
    template: ''
})
export class SensorDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private sensorPopupService: SensorPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.sensorPopupService
                .open(SensorDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
