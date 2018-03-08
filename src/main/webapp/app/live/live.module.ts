import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { WestationSharedModule } from '../shared';

import { LIVE_ROUTE, LiveComponent } from './';

import { AgmCoreModule } from '@agm/core';


import { KeysPipe, StateToStringPipe, StateToClassPipe } from './pipes';

import {
    MqttMessage,
    MqttModule,
    MqttService,
    MqttServiceOptions,
    OnMessageEvent
} from 'ngx-mqtt';

export const MQTT_SERVICE_OPTIONS: MqttServiceOptions = {
    hostname: MQTT_SERVER,
    port: 1884 //WEBSOCKET PORT OF THE MQTT
};

export function mqttServiceFactory() {
    return new MqttService(MQTT_SERVICE_OPTIONS);
}

@NgModule({
    imports: [
        WestationSharedModule,
        RouterModule.forChild([ LIVE_ROUTE ]),
        MqttModule.forRoot({
            provide: MqttService,
            useFactory: mqttServiceFactory
        }),
        AgmCoreModule.forRoot({
            apiKey: YOUR_API_KEY
        })
    ],
    declarations: [
        LiveComponent,
        KeysPipe,
        StateToStringPipe,
        StateToClassPipe
    ],
    entryComponents: [
    ],
    providers: [
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class WestationLiveModule {}
