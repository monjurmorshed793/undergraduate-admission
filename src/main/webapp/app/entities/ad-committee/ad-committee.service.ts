import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IAdCommittee } from 'app/shared/model/ad-committee.model';

type EntityResponseType = HttpResponse<IAdCommittee>;
type EntityArrayResponseType = HttpResponse<IAdCommittee[]>;

@Injectable({ providedIn: 'root' })
export class AdCommitteeService {
  public resourceUrl = SERVER_API_URL + 'api/ad-committees';

  constructor(protected http: HttpClient) {}

  create(adCommittee: IAdCommittee): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(adCommittee);
    return this.http
      .post<IAdCommittee>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(adCommittee: IAdCommittee): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(adCommittee);
    return this.http
      .put<IAdCommittee>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IAdCommittee>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IAdCommittee[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(adCommittee: IAdCommittee): IAdCommittee {
    const copy: IAdCommittee = Object.assign({}, adCommittee, {
      createdOn: adCommittee.createdOn && adCommittee.createdOn.isValid() ? adCommittee.createdOn.format(DATE_FORMAT) : undefined,
      modifiedOn: adCommittee.modifiedOn && adCommittee.modifiedOn.isValid() ? adCommittee.modifiedOn.format(DATE_FORMAT) : undefined
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
      res.body.forEach((adCommittee: IAdCommittee) => {
        adCommittee.createdOn = adCommittee.createdOn ? moment(adCommittee.createdOn) : undefined;
        adCommittee.modifiedOn = adCommittee.modifiedOn ? moment(adCommittee.modifiedOn) : undefined;
      });
    }
    return res;
  }
}
