import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IAdCommittee, AdCommittee } from 'app/shared/model/ad-committee.model';
import { AdCommitteeService } from './ad-committee.service';
import { AdCommitteeComponent } from './ad-committee.component';
import { AdCommitteeDetailComponent } from './ad-committee-detail.component';
import { AdCommitteeUpdateComponent } from './ad-committee-update.component';

@Injectable({ providedIn: 'root' })
export class AdCommitteeResolve implements Resolve<IAdCommittee> {
  constructor(private service: AdCommitteeService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IAdCommittee> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((adCommittee: HttpResponse<AdCommittee>) => {
          if (adCommittee.body) {
            return of(adCommittee.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new AdCommittee());
  }
}

export const adCommitteeRoute: Routes = [
  {
    path: '',
    component: AdCommitteeComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'AdCommittees'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: AdCommitteeDetailComponent,
    resolve: {
      adCommittee: AdCommitteeResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'AdCommittees'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: AdCommitteeUpdateComponent,
    resolve: {
      adCommittee: AdCommitteeResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'AdCommittees'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: AdCommitteeUpdateComponent,
    resolve: {
      adCommittee: AdCommitteeResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'AdCommittees'
    },
    canActivate: [UserRouteAccessService]
  }
];
