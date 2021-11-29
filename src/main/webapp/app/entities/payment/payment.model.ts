import * as dayjs from 'dayjs';
import { ICourse } from 'app/entities/course/course.model';
import { IPaymentUser } from 'app/entities/payment-user/payment-user.model';

export interface IPayment {
  id?: number;
  deadline?: dayjs.Dayjs | null;
  date?: dayjs.Dayjs | null;
  course?: ICourse | null;
  paymentUsers?: IPaymentUser[] | null;
}

export class Payment implements IPayment {
  constructor(
    public id?: number,
    public deadline?: dayjs.Dayjs | null,
    public date?: dayjs.Dayjs | null,
    public course?: ICourse | null,
    public paymentUsers?: IPaymentUser[] | null
  ) {}
}

export function getPaymentIdentifier(payment: IPayment): number | undefined {
  return payment.id;
}
