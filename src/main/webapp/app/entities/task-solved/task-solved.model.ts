import * as dayjs from 'dayjs';
import { IUser } from 'app/entities/user/user.model';
import { ITask } from 'app/entities/task/task.model';

export interface ITaskSolved {
  id?: number;
  pointGrade?: number | null;
  content?: string | null;
  deadline?: dayjs.Dayjs | null;
  sendDay?: dayjs.Dayjs | null;
  answer?: string | null;
  attachmentContentType?: string | null;
  attachment?: string | null;
  user?: IUser | null;
  task?: ITask | null;
}

export class TaskSolved implements ITaskSolved {
  constructor(
    public id?: number,
    public pointGrade?: number | null,
    public content?: string | null,
    public deadline?: dayjs.Dayjs | null,
    public sendDay?: dayjs.Dayjs | null,
    public answer?: string | null,
    public attachmentContentType?: string | null,
    public attachment?: string | null,
    public user?: IUser | null,
    public task?: ITask | null
  ) {}
}

export function getTaskSolvedIdentifier(taskSolved: ITaskSolved): number | undefined {
  return taskSolved.id;
}
