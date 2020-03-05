import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { ISemester } from 'app/shared/model/semester.model';

type EntityResponseType = HttpResponse<ISemester>;
type EntityArrayResponseType = HttpResponse<ISemester[]>;

@Injectable({ providedIn: 'root' })
export class SemesterService {
  public resourceUrl = SERVER_API_URL + 'api/semesters';

  constructor(protected http: HttpClient) {}

  create(semester: ISemester): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(semester);
    return this.http
      .post<ISemester>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(semester: ISemester): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(semester);
    return this.http
      .put<ISemester>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ISemester>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ISemester[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(semester: ISemester): ISemester {
    const copy: ISemester = Object.assign({}, semester, {
      startDate: semester.startDate && semester.startDate.isValid() ? semester.startDate.format(DATE_FORMAT) : undefined,
      endDate: semester.endDate && semester.endDate.isValid() ? semester.endDate.format(DATE_FORMAT) : undefined,
      createdOn: semester.createdOn && semester.createdOn.isValid() ? semester.createdOn.format(DATE_FORMAT) : undefined,
      modifiedOn: semester.modifiedOn && semester.modifiedOn.isValid() ? semester.modifiedOn.format(DATE_FORMAT) : undefined
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.startDate = res.body.startDate ? moment(res.body.startDate) : undefined;
      res.body.endDate = res.body.endDate ? moment(res.body.endDate) : undefined;
      res.body.createdOn = res.body.createdOn ? moment(res.body.createdOn) : undefined;
      res.body.modifiedOn = res.body.modifiedOn ? moment(res.body.modifiedOn) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((semester: ISemester) => {
        semester.startDate = semester.startDate ? moment(semester.startDate) : undefined;
        semester.endDate = semester.endDate ? moment(semester.endDate) : undefined;
        semester.createdOn = semester.createdOn ? moment(semester.createdOn) : undefined;
        semester.modifiedOn = semester.modifiedOn ? moment(semester.modifiedOn) : undefined;
      });
    }
    return res;
  }
}
