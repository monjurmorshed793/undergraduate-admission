import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { ISemester } from 'app/shared/model/semester.model';
import { SemesterService } from 'app/entities/semester/semester.service';

type EntityResponseType = HttpResponse<ISemester>;
type EntityArrayResponseType = HttpResponse<ISemester[]>;

@Injectable({ providedIn: 'root' })
export class SemesterExtendedService extends SemesterService {
  public resourceUrlExtended = SERVER_API_URL + 'api/extended/semesters';

  constructor(protected http: HttpClient) {
    super(http);
  }

  create(semester: ISemester): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(semester);
    return this.http
      .post<ISemester>(this.resourceUrlExtended, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(semester: ISemester): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(semester);
    return this.http
      .put<ISemester>(this.resourceUrlExtended, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }
}
