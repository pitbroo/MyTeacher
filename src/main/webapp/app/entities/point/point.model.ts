import * as dayjs from 'dayjs';
import { IUser } from 'app/entities/user/user.model';

export interface IPoint {
  id?: number;
  date?: dayjs.Dayjs | null;
  value?: number | null;
  user?: IUser | null;
}

export class Point implements IPoint {
  constructor(public id?: number, public date?: dayjs.Dayjs | null, public value?: number | null, public user?: IUser | null) {}
}

export function getPointIdentifier(point: IPoint): number | undefined {
  return point.id;
}
