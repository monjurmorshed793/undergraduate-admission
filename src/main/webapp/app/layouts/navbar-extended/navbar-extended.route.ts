import { Route } from '@angular/router';

import { NavbarExtendedComponent } from './navbar-extended.component';

export const navbarExtendedRoute: Route = {
  path: '',
  component: NavbarExtendedComponent,
  outlet: 'navbar'
};
