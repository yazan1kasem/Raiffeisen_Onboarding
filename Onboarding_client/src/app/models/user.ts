export class User {
  id?: string;
  username: string;
  password?: string;
  createdAt?: Date;
  updatedAt?: Date;
  role?:Role;

  constructor(
    username: string,
    password: string,
    id?: string,
    createdAt?: Date,
    updatedAt?: Date,
    role?:Role
  ) {
    this.username = username;
    this.password = password;
    this.id = id;
    this.createdAt = createdAt;
    this.updatedAt = updatedAt;
    this.role = role;
  }
}
export enum Role {
  ADMIN = 'ADMIN',
  USER = 'USER'
}
