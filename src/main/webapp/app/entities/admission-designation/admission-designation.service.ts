import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IAdmissionDesignation } from 'app/shared/model/admission-designation.model';

type EntityResponseType = HttpResponse<IAdmissionDesignation>;
type EntityArrayResponseType = HttpResponse<IAdmissionDesignation[]>;

@Injectable({ providedIn: 'root' })
export class AdmissionDesignationService {
  public resourceUrl = SERVER_API_URL + 'api/admission-designations';

  constructor(protected http: HttpClient) {}

  create(admissionDesignation: IAdmissionDesignation): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(admissionDesignation);
    return this.http
      .post<IAdmissionDesignation>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(admissionDesignation: IAdmissionDesignation): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(admissionDesignation);
    return this.http
      .put<IAdmissionDesignation>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IAdmissionDesignation>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IAdmissionDesignation[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(admissionDesignation: IAdmissionDesignation): IAdmissionDesignation {
    const copy: IAdmissionDesignation = Object.assign({}, admissionDesignation, {
      createdOn:
        admissionDesignation.createdOn && admissionDesignation.createdOn.isValid()
          ? admissionDesignation.createdOn.format(DATE_FORMAT)
          : undefined,
      modifiedOn:
        admissionDesignation.modifiedOn && admissionDesignation.modifiedOn.isValid()
          ? admissionDesignation.modifiedOn.format(DATE_FORMAT)
          : undefined
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
      res.body.forEach((admissionDesignation: IAdmissionDesignation) => {
        admissionDesignation.createdOn = admissionDesignation.createdOn ? moment(admissionDesignation.createdOn) : undefined;
        admissionDesignation.modifiedOn = admissionDesignation.modifiedOn ? moment(admissionDesignation.modifiedOn) : undefined;
      });
    }
    return res;
  }
}
