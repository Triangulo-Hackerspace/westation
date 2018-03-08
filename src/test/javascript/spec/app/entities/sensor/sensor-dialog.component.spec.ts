/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable } from 'rxjs/Observable';
import { JhiEventManager } from 'ng-jhipster';

import { WestationTestModule } from '../../../test.module';
import { SensorDialogComponent } from '../../../../../../main/webapp/app/entities/sensor/sensor-dialog.component';
import { SensorService } from '../../../../../../main/webapp/app/entities/sensor/sensor.service';
import { Sensor } from '../../../../../../main/webapp/app/entities/sensor/sensor.model';

describe('Component Tests', () => {

    describe('Sensor Management Dialog Component', () => {
        let comp: SensorDialogComponent;
        let fixture: ComponentFixture<SensorDialogComponent>;
        let service: SensorService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [WestationTestModule],
                declarations: [SensorDialogComponent],
                providers: [
                    SensorService
                ]
            })
            .overrideTemplate(SensorDialogComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(SensorDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(SensorService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        const entity = new Sensor(123);
                        spyOn(service, 'update').and.returnValue(Observable.of(entity));
                        comp.sensor = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.update).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'sensorListModification', content: 'OK'});
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    })
                )
            );

            it('Should call create service on save for new entity',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        const entity = new Sensor();
                        spyOn(service, 'create').and.returnValue(Observable.of(entity));
                        comp.sensor = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.create).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'sensorListModification', content: 'OK'});
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    })
                )
            );
        });
    });

});
