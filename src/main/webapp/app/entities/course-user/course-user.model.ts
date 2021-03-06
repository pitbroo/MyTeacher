
import { ICourse } from 'app/entities/course/course.model';
import {IUser} from "../../admin/user-management/user-management.model";

export interface ICourseUser {
  id?: number;
  user?: IUser | null;
  course?: ICourse | null;
}

export class CourseUser implements ICourseUser {
  constructor(public id?: number, public user?: IUser | null, public course?: ICourse | null) {}
}

export function getCourseUserIdentifier(courseUser: ICourseUser): number | undefined {
  return courseUser.id;
}
