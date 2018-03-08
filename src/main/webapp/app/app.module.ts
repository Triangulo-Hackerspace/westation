import './vendor.ts';

import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { Ng2Webstorage } from 'ngx-webstorage';

import { WestationSharedModule, UserRouteAccessService } from './shared';
import { WestationAppRoutingModule} from './app-routing.module';
import { WestationHomeModule } from './home/home.module';
import { WestationAdminModule } from './admin/admin.module';
import { WestationAccountModule } from './account/account.module';
import { WestationEntityModule } from './entities/entity.module';
import { WestationLiveModule } from './live/live.module';
import { customHttpProvider } from './blocks/interceptor/http.provider';
import { PaginationConfig } from './blocks/config/uib-pagination.config';

// jhipster-needle-angular-add-module-import JHipster will add new module here

import {
    JhiMainComponent,
    NavbarComponent,
    FooterComponent,
    ProfileService,
    PageRibbonComponent,
    ActiveMenuDirective,
    ErrorComponent
} from './layouts';

@NgModule({
    imports: [
        BrowserModule,
        WestationAppRoutingModule,
        Ng2Webstorage.forRoot({ prefix: 'jhi', separator: '-'}),
        WestationSharedModule,
        WestationHomeModule,
        WestationAdminModule,
        WestationAccountModule,
        WestationEntityModule,
        // jhipster-needle-angular-add-module JHipster will add new module here
        WestationLiveModule,
    ],
    declarations: [
        JhiMainComponent,
        NavbarComponent,
        ErrorComponent,
        PageRibbonComponent,
        ActiveMenuDirective,
        FooterComponent
    ],
    providers: [
        ProfileService,
        customHttpProvider(),
        PaginationConfig,
        UserRouteAccessService
    ],
    bootstrap: [ JhiMainComponent ]
})
export class WestationAppModule {}
