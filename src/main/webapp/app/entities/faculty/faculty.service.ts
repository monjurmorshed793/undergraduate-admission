import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IFaculty } from 'app/shared/model/faculty.model';

type EntityResponseType = HttpResponse<IFaculty>;
type EntityArrayResponseType = HttpResponse<IFaculty[]>;

@Injectable({ providedIn: 'root' })
export class FacultyService {
  public resourceUrl = SERVER_API_URL + 'api/faculties';

  constructor(protected http: HttpClient) {}

  create(faculty: IFaculty): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(faculty);
    return this.http
      .post<IFaculty>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(faculty: IFaculty): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(faculty);
    return this.http
      .put<IFaculty>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IFaculty>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IFaculty[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(faculty: IFaculty): IFaculty {
    const copy: IFaculty = Object.assign({}, faculty, {
      createdOn: faculty.createdOn && faculty.createdOn.isValid() ? faculty.createdOn.format(DATE_FORMAT) : undefined,
      modifiedOn: faculty.modifiedOn && faculty.modifiedOn.isValid() ? faculty.modifiedOn.format(DATE_FORMAT) : undefined
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
      res.body.forEach((faculty: IFaculty) => {
        faculty.createdOn = faculty.createdOn ? moment(faculty.createdOn) : undefined;
        faculty.modifiedOn = faculty.modifiedOn ? moment(faculty.modifiedOn) : undefined;
      });
    }
    return res;
  }
}
