import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IAdmissionDesignation, AdmissionDesignation } from 'app/shared/model/admission-designation.model';
import { AdmissionDesignationService } from './admission-designation.service';
import { AdmissionDesignationComponent } from './admission-designation.component';
import { AdmissionDesignationDetailComponent } from './admission-designation-detail.component';
import { AdmissionDesignationUpdateComponent } from './admission-designation-update.component';

@Injectable({ providedIn: 'root' })
export class AdmissionDesignationResolve implements Resolve<IAdmissionDesignation> {
  constructor(private service: AdmissionDesignationService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IAdmissionDesignation> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((admissionDesignation: HttpResponse<AdmissionDesignation>) => {
          if (admissionDesignation.body) {
            return of(admissionDesignation.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new AdmissionDesignation());
  }
}

export const admissionDesignationRoute: Routes = [
  {
    path: '',
    component: AdmissionDesignationComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'AdmissionDesignations'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: AdmissionDesignationDetailComponent,
    resolve: {
      admissionDesignation: AdmissionDesignationResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'AdmissionDesignations'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: AdmissionDesignationUpdateComponent,
    resolve: {
      admissionDesignation: AdmissionDesignationResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'AdmissionDesignations'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: AdmissionDesignationUpdateComponent,
    resolve: {
      admissionDesignation: AdmissionDesignationResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'AdmissionDesignations'
    },
    canActivate: [UserRouteAccessService]
  }
];
