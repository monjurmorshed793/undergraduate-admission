import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IFaProgram, FaProgram } from 'app/shared/model/fa-program.model';
import { FaProgramService } from './fa-program.service';
import { FaProgramComponent } from './fa-program.component';
import { FaProgramDetailComponent } from './fa-program-detail.component';
import { FaProgramUpdateComponent } from './fa-program-update.component';

@Injectable({ providedIn: 'root' })
export class FaProgramResolve implements Resolve<IFaProgram> {
  constructor(private service: FaProgramService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IFaProgram> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((faProgram: HttpResponse<FaProgram>) => {
          if (faProgram.body) {
            return of(faProgram.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new FaProgram());
  }
}

export const faProgramRoute: Routes = [
  {
    path: '',
    component: FaProgramComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'FaPrograms'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: FaProgramDetailComponent,
    resolve: {
      faProgram: FaProgramResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'FaPrograms'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: FaProgramUpdateComponent,
    resolve: {
      faProgram: FaProgramResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'FaPrograms'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: FaProgramUpdateComponent,
    resolve: {
      faProgram: FaProgramResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'FaPrograms'
    },
    canActivate: [UserRouteAccessService]
  }
];
