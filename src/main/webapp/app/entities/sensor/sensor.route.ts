import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil } from 'ng-jhipster';

import { UserRouteAccessService } from '../../shared';
import { SensorComponent } from './sensor.component';
import { SensorDetailComponent } from './sensor-detail.component';
import { SensorPopupComponent } from './sensor-dialog.component';
import { SensorDeletePopupComponent } from './sensor-delete-dialog.component';

@Injectable()
export class SensorResolvePagingParams implements Resolve<any> {

    constructor(private paginationUtil: JhiPaginationUtil) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const page = route.queryParams['page'] ? route.queryParams['page'] : '1';
        const sort = route.queryParams['sort'] ? route.queryParams['sort'] : 'id,asc';
        return {
            page: this.paginationUtil.parsePage(page),
            predicate: this.paginationUtil.parsePredicate(sort),
            ascending: this.paginationUtil.parseAscending(sort)
      };
    }
}

export const sensorRoute: Routes = [
    {
        path: 'sensor',
        component: SensorComponent,
        resolve: {
            'pagingParams': SensorResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'westationApp.sensor.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'sensor/:id',
        component: SensorDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'westationApp.sensor.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const sensorPopupRoute: Routes = [
    {
        path: 'sensor-new',
        component: SensorPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'westationApp.sensor.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'sensor/:id/edit',
        component: SensorPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'westationApp.sensor.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'sensor/:id/delete',
        component: SensorDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'westationApp.sensor.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
