import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { WestationStationModule } from './station/station.module';
import { WestationSensorModule } from './sensor/sensor.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    imports: [
        WestationStationModule,
        WestationSensorModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class WestationEntityModule {}
