import {User} from "./user";
import {Device} from "./device";
import {Department} from "./department";
import {Position} from "./position";

export class Checklist {
  id: string;
  users: User[];
  saved: boolean;
  ueberschrift: string;
  device: Device;
  department: Department;
  position: Position;

  constructor(
    id: string,
    users: User[],
    saved: boolean,
    ueberschrift: string,
    device: Device,
    department: Department,
    position: Position
  ) {
    this.id = id;
    this.users = users;
    this.saved = saved;
    this.ueberschrift = ueberschrift;
    this.device = device;
    this.department = department;
    this.position = position;
  }
}
