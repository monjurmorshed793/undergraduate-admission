import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { ITotalSeat } from 'app/shared/model/total-seat.model';

type EntityResponseType = HttpResponse<ITotalSeat>;
type EntityArrayResponseType = HttpResponse<ITotalSeat[]>;

@Injectable({ providedIn: 'root' })
export class TotalSeatService {
  public resourceUrl = SERVER_API_URL + 'api/total-seats';

  constructor(protected http: HttpClient) {}

  create(totalSeat: ITotalSeat): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(totalSeat);
    return this.http
      .post<ITotalSeat>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(totalSeat: ITotalSeat): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(totalSeat);
    return this.http
      .put<ITotalSeat>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ITotalSeat>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ITotalSeat[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(totalSeat: ITotalSeat): ITotalSeat {
    const copy: ITotalSeat = Object.assign({}, totalSeat, {
      createdOn: totalSeat.createdOn && totalSeat.createdOn.isValid() ? totalSeat.createdOn.format(DATE_FORMAT) : undefined,
      modifiedOn: totalSeat.modifiedOn && totalSeat.modifiedOn.isValid() ? totalSeat.modifiedOn.format(DATE_FORMAT) : undefined
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.createdOn = res.body.createdOn ? moment(res.body.createdOn) : undefined;
      res.body.modifiedOn = res.body.modifiedOn ? moment(res.body.modifiedOn) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((totalSeat: ITotalSeat) => {
        totalSeat.createdOn = totalSeat.createdOn ? moment(totalSeat.createdOn) : undefined;
        totalSeat.modifiedOn = totalSeat.modifiedOn ? moment(totalSeat.modifiedOn) : undefined;
      });
    }
    return res;
  }
}
