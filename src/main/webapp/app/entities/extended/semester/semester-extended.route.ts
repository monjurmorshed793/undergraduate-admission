import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { ISemester, Semester } from 'app/shared/model/semester.model';
import { SemesterExtendedService } from './semester-extended.service';
import { SemesterExtendedComponent } from './semester-extended.component';
import { SemesterExtendedDetailComponent } from './semester-extended-detail.component';
import { SemesterExtendedUpdateComponent } from './semester-extended-update.component';

@Injectable({ providedIn: 'root' })
export class SemesterExtendedResolve implements Resolve<ISemester> {
  constructor(private service: SemesterExtendedService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ISemester> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((semester: HttpResponse<Semester>) => {
          if (semester.body) {
            return of(semester.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Semester());
  }
}

export const semesterExtendedRoute: Routes = [
  {
    path: '',
    component: SemesterExtendedComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Semesters'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: SemesterExtendedDetailComponent,
    resolve: {
      semester: SemesterExtendedResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Semesters'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: SemesterExtendedUpdateComponent,
    resolve: {
      semester: SemesterExtendedResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Semesters'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: SemesterExtendedUpdateComponent,
    resolve: {
      semester: SemesterExtendedResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Semesters'
    },
    canActivate: [UserRouteAccessService]
  }
];
