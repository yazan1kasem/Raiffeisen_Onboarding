import {Item} from "./item";

export class UserChecklistItems {
  id?: string;
  originalItem: Item;
  isChecked: boolean;
  createdAt?: Date;
  updatedAt?: Date;

  constructor(
    originalItem: Item,
    isChecked: boolean,
    id?: string,
    createdAt?: Date,
    updatedAt?: Date
  ) {
    this.id = id;
    this.originalItem = originalItem;
    this.isChecked = isChecked;
    this.createdAt = createdAt;
    this.updatedAt = updatedAt;
  }
}
