
export class User {
  id: string;
  name: string;
  admin: boolean;

  constructor(id: string, name: string, admin: boolean) {
    this.id = id;
    this.name = name;
    this.admin = admin;
  }
}
