import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { WestationSharedModule } from '../../shared';
import {
    SensorService,
    SensorPopupService,
    SensorComponent,
    SensorDetailComponent,
    SensorDialogComponent,
    SensorPopupComponent,
    SensorDeletePopupComponent,
    SensorDeleteDialogComponent,
    sensorRoute,
    sensorPopupRoute,
    SensorResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...sensorRoute,
    ...sensorPopupRoute,
];

@NgModule({
    imports: [
        WestationSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        SensorComponent,
        SensorDetailComponent,
        SensorDialogComponent,
        SensorDeleteDialogComponent,
        SensorPopupComponent,
        SensorDeletePopupComponent,
    ],
    entryComponents: [
        SensorComponent,
        SensorDialogComponent,
        SensorPopupComponent,
        SensorDeleteDialogComponent,
        SensorDeletePopupComponent,
    ],
    providers: [
        SensorService,
        SensorPopupService,
        SensorResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class WestationSensorModule {}
