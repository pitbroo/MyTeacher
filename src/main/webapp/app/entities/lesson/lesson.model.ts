import * as dayjs from 'dayjs';
import { ICourse } from 'app/entities/course/course.model';
import { ITask } from 'app/entities/task/task.model';

export interface ILesson {
  id?: number;
  name?: string;
  status?: string | null;
  dateStart?: dayjs.Dayjs | null;
  dateEnd?: dayjs.Dayjs | null;
  classroomOrAddres?: string | null;
  course?: ICourse | null;
  tasks?: ITask[] | null;
}

export class Lesson implements ILesson {
  constructor(
    public id?: number,
    public name?: string,
    public status?: string | null,
    public dateStart?: dayjs.Dayjs | null,
    public dateEnd?: dayjs.Dayjs | null,
    public classroomOrAddres?: string | null,
    public course?: ICourse | null,
    public tasks?: ITask[] | null
  ) {}
}

export function getLessonIdentifier(lesson: ILesson): number | undefined {
  return lesson.id;
}
