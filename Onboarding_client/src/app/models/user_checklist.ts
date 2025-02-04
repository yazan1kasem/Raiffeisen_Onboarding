import {User} from "./user";
import {Checklist} from "./checklist";
import {UserChecklistItems} from "./user_checklist_items";

export class UserChecklist {
  id: string;
  ueberschrift: string;
  originalChecklist: Checklist | null;
  status: ChecklistStatus;
  userPermissions: { [userId: string]: boolean };
  isLocked: boolean;
  createdAt: Date;
  updatedAt: Date;
  item: UserChecklistItems[];

  constructor(
    id: string,
    originalChecklist: Checklist | null,
    status: ChecklistStatus,
    ueberschrift: string,
  userPermissions: { [userId: string]: boolean },
    isLocked: boolean,
    createdAt: Date,
    updatedAt: Date,
    item: UserChecklistItems[]
  ) {
    this.id = id;
    this.originalChecklist = originalChecklist;
    this.status = status;
    this.userPermissions = userPermissions;
    this.isLocked = isLocked;
    this.createdAt = createdAt;
    this.updatedAt = updatedAt;
    this.ueberschrift = ueberschrift;
    this.item = item;
  }
}
export enum ChecklistStatus {
  IN_PROGRESS = "IN_PROGRESS",
  COMPLETED = "COMPLETED",
}
