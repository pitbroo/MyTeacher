export interface IUser {
  id?: number;
  login?: string;
  name?: string;
  surname?: string;
}

export class User implements IUser {
  constructor(
    public id: number,
    public login: string,
    public name?: string,
    public surname?: string
    ) {}
}

export function getUserIdentifier(user: IUser): number | undefined {
  return user.id;
}
// export function getUserLogin(user: IUser): string | undefined {
//   return user.login;
// }
// export function getUserNameAndSurname(user: IUser): string | undefined {
//   return user.name;
// }
