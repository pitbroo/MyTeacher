import { IUser } from 'app/entities/user/user.model';
import { IEmailNotification } from 'app/entities/email-notification/email-notification.model';

export interface IEmailNotificationUser {
  id?: number;
  user?: IUser | null;
  emailNotification?: IEmailNotification | null;
}

export class EmailNotificationUser implements IEmailNotificationUser {
  constructor(public id?: number, public user?: IUser | null, public emailNotification?: IEmailNotification | null) {}
}

export function getEmailNotificationUserIdentifier(emailNotificationUser: IEmailNotificationUser): number | undefined {
  return emailNotificationUser.id;
}
