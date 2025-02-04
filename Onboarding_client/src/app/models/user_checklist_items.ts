export class UserChecklistItems {
  id?: string;
  originalItemId: string;
  isChecked: boolean;
  createdAt?: Date;
  updatedAt?: Date;

  constructor(
    originalItemId: string,
    isChecked: boolean,
    id?: string,
    createdAt?: Date,
    updatedAt?: Date
  ) {
    this.id = id;
    this.originalItemId = originalItemId;
    this.isChecked = isChecked;
    this.createdAt = createdAt;
    this.updatedAt = updatedAt;
  }
}
