import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { ITotalSeat, TotalSeat } from 'app/shared/model/total-seat.model';
import { TotalSeatService } from './total-seat.service';
import { TotalSeatComponent } from './total-seat.component';
import { TotalSeatDetailComponent } from './total-seat-detail.component';
import { TotalSeatUpdateComponent } from './total-seat-update.component';

@Injectable({ providedIn: 'root' })
export class TotalSeatResolve implements Resolve<ITotalSeat> {
  constructor(private service: TotalSeatService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ITotalSeat> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((totalSeat: HttpResponse<TotalSeat>) => {
          if (totalSeat.body) {
            return of(totalSeat.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new TotalSeat());
  }
}

export const totalSeatRoute: Routes = [
  {
    path: '',
    component: TotalSeatComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'TotalSeats'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: TotalSeatDetailComponent,
    resolve: {
      totalSeat: TotalSeatResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'TotalSeats'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: TotalSeatUpdateComponent,
    resolve: {
      totalSeat: TotalSeatResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'TotalSeats'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: TotalSeatUpdateComponent,
    resolve: {
      totalSeat: TotalSeatResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'TotalSeats'
    },
    canActivate: [UserRouteAccessService]
  }
];
