import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IFaProgram } from 'app/shared/model/fa-program.model';

type EntityResponseType = HttpResponse<IFaProgram>;
type EntityArrayResponseType = HttpResponse<IFaProgram[]>;

@Injectable({ providedIn: 'root' })
export class FaProgramService {
  public resourceUrl = SERVER_API_URL + 'api/fa-programs';

  constructor(protected http: HttpClient) {}

  create(faProgram: IFaProgram): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(faProgram);
    return this.http
      .post<IFaProgram>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(faProgram: IFaProgram): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(faProgram);
    return this.http
      .put<IFaProgram>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IFaProgram>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IFaProgram[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(faProgram: IFaProgram): IFaProgram {
    const copy: IFaProgram = Object.assign({}, faProgram, {
      createdOn: faProgram.createdOn && faProgram.createdOn.isValid() ? faProgram.createdOn.format(DATE_FORMAT) : undefined,
      modifiedOn: faProgram.modifiedOn && faProgram.modifiedOn.isValid() ? faProgram.modifiedOn.format(DATE_FORMAT) : undefined
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
      res.body.forEach((faProgram: IFaProgram) => {
        faProgram.createdOn = faProgram.createdOn ? moment(faProgram.createdOn) : undefined;
        faProgram.modifiedOn = faProgram.modifiedOn ? moment(faProgram.modifiedOn) : undefined;
      });
    }
    return res;
  }
}
