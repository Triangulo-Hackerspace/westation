import { Route } from '@angular/router';

import { LiveComponent } from './';

export const LIVE_ROUTE: Route = {
    path: 'live',
    component: LiveComponent,
    data: {
        authorities: [],
        pageTitle: 'global.title'
    }
};
