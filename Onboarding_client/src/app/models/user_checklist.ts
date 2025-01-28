import {User} from "./user";
import {user_Items} from "./user_Items";
import {Checklist} from "./checklist";

export class UserChecklist {
  id: string;
  originalChecklist: Checklist | null;
  user: User | null;
  items: user_Items[];
  status: ChecklistStatus;
  userPermissions: { [userId: string]: boolean };
  isLocked: boolean;
  createdAt: Date;
  updatedAt: Date;

  constructor(
    id: string,
    originalChecklist: Checklist | null,
    user: User | null,
    items: user_Items[],
    status: ChecklistStatus,
    userPermissions: { [userId: string]: boolean },
    isLocked: boolean,
    createdAt: Date,
    updatedAt: Date
  ) {
    this.id = id;
    this.originalChecklist = originalChecklist;
    this.user = user;
    this.items = items;
    this.status = status;
    this.userPermissions = userPermissions;
    this.isLocked = isLocked;
    this.createdAt = createdAt;
    this.updatedAt = updatedAt;
  }
}
export enum ChecklistStatus {
  IN_PROGRESS = "IN_PROGRESS",
  COMPLETED = "COMPLETED",
}
