import * as dayjs from 'dayjs';
import { IEmailNotificationUser } from 'app/entities/email-notification-user/email-notification-user.model';

export interface IEmailNotification {
  id?: number;
  content?: string | null;
  time?: dayjs.Dayjs | null;
  teacher?: string | null;
  emailNotificationUsers?: IEmailNotificationUser[] | null;
}

export class EmailNotification implements IEmailNotification {
  constructor(
    public id?: number,
    public content?: string | null,
    public time?: dayjs.Dayjs | null,
    public teacher?: string | null,
    public emailNotificationUsers?: IEmailNotificationUser[] | null
  ) {}
}

export function getEmailNotificationIdentifier(emailNotification: IEmailNotification): number | undefined {
  return emailNotification.id;
}
