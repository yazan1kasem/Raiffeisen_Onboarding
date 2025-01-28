export class Item {
  id: string;
  name: string;
  type: string;
  suchbegriff: string;

  constructor(
    id: string,
    name: string,
    type: string,
    suchbegriff: string
  ) {
    this.id = id;
    this.name = name;
    this.type = type;
    this.suchbegriff = suchbegriff;
  }
}
