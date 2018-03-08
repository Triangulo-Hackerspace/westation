/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';

import { WestationTestModule } from '../../../test.module';
import { StationDetailComponent } from '../../../../../../main/webapp/app/entities/station/station-detail.component';
import { StationService } from '../../../../../../main/webapp/app/entities/station/station.service';
import { Station } from '../../../../../../main/webapp/app/entities/station/station.model';

describe('Component Tests', () => {

    describe('Station Management Detail Component', () => {
        let comp: StationDetailComponent;
        let fixture: ComponentFixture<StationDetailComponent>;
        let service: StationService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [WestationTestModule],
                declarations: [StationDetailComponent],
                providers: [
                    StationService
                ]
            })
            .overrideTemplate(StationDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(StationDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(StationService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new Station(123)));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.station).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
