import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { WestationSharedModule } from '../../shared';
import {
    StationService,
    StationPopupService,
    StationComponent,
    StationDetailComponent,
    StationDialogComponent,
    StationPopupComponent,
    StationDeletePopupComponent,
    StationDeleteDialogComponent,
    stationRoute,
    stationPopupRoute,
    StationResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...stationRoute,
    ...stationPopupRoute,
];

@NgModule({
    imports: [
        WestationSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        StationComponent,
        StationDetailComponent,
        StationDialogComponent,
        StationDeleteDialogComponent,
        StationPopupComponent,
        StationDeletePopupComponent,
    ],
    entryComponents: [
        StationComponent,
        StationDialogComponent,
        StationPopupComponent,
        StationDeleteDialogComponent,
        StationDeletePopupComponent,
    ],
    providers: [
        StationService,
        StationPopupService,
        StationResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class WestationStationModule {}
