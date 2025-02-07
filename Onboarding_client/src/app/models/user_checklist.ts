import {User} from "./user";
import {Checklist} from "./checklist";
import {UserChecklistItems} from "./user_checklist_items";

export class UserChecklist {
  id: string;
  ueberschrift: string;
  originalChecklist: Checklist | null;
  status: ChecklistStatus;
  user: User;
  isLocked: boolean;
  createdAt: Date;
  updatedAt: Date;
  useritems: UserChecklistItems[];

  constructor(
    id: string,
    originalChecklist: Checklist | null,
    status: ChecklistStatus,
    user: User,
    ueberschrift: string,
    isLocked: boolean,
    createdAt: Date,
    updatedAt: Date,
    useritems: UserChecklistItems[]
  ) {
    this.id = id;
    this.originalChecklist = originalChecklist;
    this.status = status;
    this.user = user;
    this.isLocked = isLocked;
    this.createdAt = createdAt;
    this.updatedAt = updatedAt;
    this.ueberschrift = ueberschrift;
    this.useritems = useritems;
  }
}
export enum ChecklistStatus {
  IN_PROGRESS = "IN_PROGRESS",
  COMPLETED = "COMPLETED",
}
