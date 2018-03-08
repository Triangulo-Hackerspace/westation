/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';

import { WestationTestModule } from '../../../test.module';
import { SensorDetailComponent } from '../../../../../../main/webapp/app/entities/sensor/sensor-detail.component';
import { SensorService } from '../../../../../../main/webapp/app/entities/sensor/sensor.service';
import { Sensor } from '../../../../../../main/webapp/app/entities/sensor/sensor.model';

describe('Component Tests', () => {

    describe('Sensor Management Detail Component', () => {
        let comp: SensorDetailComponent;
        let fixture: ComponentFixture<SensorDetailComponent>;
        let service: SensorService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [WestationTestModule],
                declarations: [SensorDetailComponent],
                providers: [
                    SensorService
                ]
            })
            .overrideTemplate(SensorDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(SensorDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(SensorService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new Sensor(123)));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.sensor).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
