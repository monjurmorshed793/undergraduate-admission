import { Moment } from 'moment';
import { SemesterStatus } from 'app/shared/model/enumerations/semester-status.model';

export interface ISemester {
  id?: number;
  semesterId?: number;
  name?: string;
  shortName?: string;
  status?: SemesterStatus;
  startDate?: Moment;
  endDate?: Moment;
  createdOn?: Moment;
  modifiedOn?: Moment;
  modifiedBy?: string;
}

export class Semester implements ISemester {
  constructor(
    public id?: number,
    public semesterId?: number,
    public name?: string,
    public shortName?: string,
    public status?: SemesterStatus,
    public startDate?: Moment,
    public endDate?: Moment,
    public createdOn?: Moment,
    public modifiedOn?: Moment,
    public modifiedBy?: string
  ) {}
}
