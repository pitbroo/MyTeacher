import { IUser } from 'app/entities/user/user.model';
import { IPayment } from 'app/entities/payment/payment.model';
import { ICourseUser } from 'app/entities/course-user/course-user.model';

export interface ICourse {
  id?: number;
  name?: string | null;
  value?: number | null;
  price?: number | null;
  category?: string | null;
  description?: string | null;
  user?: IUser | null;
  payment?: IPayment | null;
  courseUsers?: ICourseUser[] | null;
}

export class Course implements ICourse {
  constructor(
    public id?: number,
    public name?: string | null,
    public value?: number | null,
    public price?: number | null,
    public category?: string | null,
    public description?: string | null,
    public user?: IUser | null,
    public payment?: IPayment | null,
    public courseUsers?: ICourseUser[] | null
  ) {}
}

export function getCourseIdentifier(course: ICourse): number | undefined {
  return course.id;
}
