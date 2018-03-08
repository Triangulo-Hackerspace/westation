/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { Headers } from '@angular/http';

import { WestationTestModule } from '../../../test.module';
import { StationComponent } from '../../../../../../main/webapp/app/entities/station/station.component';
import { StationService } from '../../../../../../main/webapp/app/entities/station/station.service';
import { Station } from '../../../../../../main/webapp/app/entities/station/station.model';

describe('Component Tests', () => {

    describe('Station Management Component', () => {
        let comp: StationComponent;
        let fixture: ComponentFixture<StationComponent>;
        let service: StationService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [WestationTestModule],
                declarations: [StationComponent],
                providers: [
                    StationService
                ]
            })
            .overrideTemplate(StationComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(StationComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(StationService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new Headers();
                headers.append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of({
                    json: [new Station(123)],
                    headers
                }));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.stations[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
