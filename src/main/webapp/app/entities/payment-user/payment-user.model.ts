import { IPayment } from 'app/entities/payment/payment.model';
import { IUser } from 'app/entities/user/user.model';

export interface IPaymentUser {
  id?: number;
  payment?: IPayment | null;
  user?: IUser | null;
}

export class PaymentUser implements IPaymentUser {
  constructor(public id?: number, public payment?: IPayment | null, public user?: IUser | null) {}
}

export function getPaymentUserIdentifier(paymentUser: IPaymentUser): number | undefined {
  return paymentUser.id;
}
