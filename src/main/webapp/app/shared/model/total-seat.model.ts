import { Moment } from 'moment';

export interface ITotalSeat {
  id?: number;
  seat?: number;
  createdOn?: Moment;
  modifiedOn?: Moment;
  modifiedBy?: string;
  facultyProgramId?: number;
}

export class TotalSeat implements ITotalSeat {
  constructor(
    public id?: number,
    public seat?: number,
    public createdOn?: Moment,
    public modifiedOn?: Moment,
    public modifiedBy?: string,
    public facultyProgramId?: number
  ) {}
}
