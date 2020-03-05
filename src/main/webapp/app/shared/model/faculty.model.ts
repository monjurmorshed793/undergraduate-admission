import { Moment } from 'moment';

export interface IFaculty {
  id?: number;
  name?: string;
  shortName?: string;
  createdOn?: Moment;
  modifiedOn?: Moment;
  modifiedBy?: string;
}

export class Faculty implements IFaculty {
  constructor(
    public id?: number,
    public name?: string,
    public shortName?: string,
    public createdOn?: Moment,
    public modifiedOn?: Moment,
    public modifiedBy?: string
  ) {}
}
