import { Route } from '@angular/router';

import { HomeExtendedComponent } from './home-extended.component';

export const HOME_ROUTE: Route = {
  path: '',
  component: HomeExtendedComponent,
  data: {
    authorities: [],
    pageTitle: 'Welcome, AUST Admission!'
  }
};
