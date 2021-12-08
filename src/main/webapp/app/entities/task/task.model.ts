import * as dayjs from 'dayjs';
import { ILesson } from 'app/entities/lesson/lesson.model';
import { ITaskSolved } from 'app/entities/task-solved/task-solved.model';

export interface ITask {
  id?: number;
  title?: string | null;
  pointGrade?: number | null;
  content?: string | null;
  deadline?: dayjs.Dayjs | null;
  attachmentContentType?: string | null;
  attachment?: string | null;
  lesson?: ILesson | null;
  taskSolveds?: ITaskSolved[] | null;
}

export class Task implements ITask {
  constructor(
    public id?: number,
    public title?: string | null,
    public pointGrade?: number | null,
    public content?: string | null,
    public deadline?: dayjs.Dayjs | null,
    public attachmentContentType?: string | null,
    public attachment?: string | null,
    public lesson?: ILesson | null,
    public taskSolveds?: ITaskSolved[] | null
  ) {}
}

export function getTaskIdentifier(task: ITask): number | undefined {
  return task.id;
}
