export class User {
  id?: string;
  username: string;
  password: string;
  isAdmin: boolean;
  createdAt?: Date;
  updatedAt?: Date;

  constructor(
    username: string,
    password: string,
    isAdmin: boolean = false,
    id?: string,
    createdAt?: Date,
    updatedAt?: Date
  ) {
    this.username = username;
    this.password = password;
    this.isAdmin = isAdmin;
    this.id = id;
    this.createdAt = createdAt;
    this.updatedAt = updatedAt;
  }
}
