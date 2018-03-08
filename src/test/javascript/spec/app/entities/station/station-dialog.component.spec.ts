/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable } from 'rxjs/Observable';
import { JhiEventManager } from 'ng-jhipster';

import { WestationTestModule } from '../../../test.module';
import { StationDialogComponent } from '../../../../../../main/webapp/app/entities/station/station-dialog.component';
import { StationService } from '../../../../../../main/webapp/app/entities/station/station.service';
import { Station } from '../../../../../../main/webapp/app/entities/station/station.model';
import { SensorService } from '../../../../../../main/webapp/app/entities/sensor';

describe('Component Tests', () => {

    describe('Station Management Dialog Component', () => {
        let comp: StationDialogComponent;
        let fixture: ComponentFixture<StationDialogComponent>;
        let service: StationService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [WestationTestModule],
                declarations: [StationDialogComponent],
                providers: [
                    SensorService,
                    StationService
                ]
            })
            .overrideTemplate(StationDialogComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(StationDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(StationService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        const entity = new Station(123);
                        spyOn(service, 'update').and.returnValue(Observable.of(entity));
                        comp.station = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.update).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'stationListModification', content: 'OK'});
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    })
                )
            );

            it('Should call create service on save for new entity',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        const entity = new Station();
                        spyOn(service, 'create').and.returnValue(Observable.of(entity));
                        comp.station = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.create).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'stationListModification', content: 'OK'});
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    })
                )
            );
        });
    });

});
