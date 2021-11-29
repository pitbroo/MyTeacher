import * as dayjs from 'dayjs';
import { IUser } from 'app/entities/user/user.model';

export interface ITimeShit {
  id?: number;
  present?: boolean | null;
  date?: dayjs.Dayjs | null;
  user?: IUser | null;
}

export class TimeShit implements ITimeShit {
  constructor(public id?: number, public present?: boolean | null, public date?: dayjs.Dayjs | null, public user?: IUser | null) {
    this.present = this.present ?? false;
  }
}

export function getTimeShitIdentifier(timeShit: ITimeShit): number | undefined {
  return timeShit.id;
}
