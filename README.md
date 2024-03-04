# blend

blend - A powerful no code framework for object mapping (a) JSON-to-JSON conversions

### What's Blend?

Imagine performing the following source to target conversion without writing a single line of code, that's the power of Blend (hold on, YAML ain't code technically :p)

#### *Source*
```
{  
 "person_name":  "Sophie Anderson", 
 "occupation":  "Data Scientist",  
 "monthly_income":  8500,  
 "date_of_birth":  "1985-07-20",  
 "place_of_birth":  "New York"  
}
```

#### *Target*
```
{
  "person": {
    "name": "Sophie Anderson",
    "occupation": "Data Scientist",
    "salary": 102000,
    "birthDetails": {
      "dob": "1985-07-20T00:00:00",
      "city": "NYC"
    }
  }
}
```
Blend takes the instructions in form of a *YAML* file, that contains an array of patches:
```
patches:
  - fromField: person_name
    toField: person.name
  - fromField: occupation
    toField: person.occupation
  - fromField: monthly_income
    toField: person.salary
    transform:
      name: Multiply
      args: 12
  - fromField: date_of_birth
    toField: person.birthDetails.dob
    transform:
      name: Date
      args: "YY-mm-dd, YYYY-MM-DDTHH:mm:SS"
  - fromField: place_of_birth
    toField: person.birthDetails.city
    transform:
      name: Map
      args: '{"New York": "NYC", "Los Angeles": "LA"}'

```

Here,
 `fromField` refers to the navigation path of the source JSON
 `toField` refers to required navigation path of the target JSON
 `transform` is an optional parameter that is used to transform the value present in `fromField` before mapping it to `toField`

### Transforms 

Blend currently supports the following transforms:
#### 1. Map 
Maps the value in `fromField` as per the reference map passed in args. Refer `place_of_birth` patch in the above example.
#### 2. Date 
Converts the date string in `fromField` from format `args[0]` to `args[1]`. Refer `date_of_birth` 
#### 3. StringToArray 
Converts the  string in `fromField` to an array, assuming `args[0]` as the delimiter
`"85, Johnson's Street, Bangalore"` ⇒ `[ "85",  "Johnson's Street",  "Bangalore" ]`
#### 4. ObjectToSingleNodeArray
Converts the JSON object in `fromField` to an array with single occurrence.
```
{
   "address" : {
		"city": "Bangalore"
	}
}
```

||
v

```
{
   "address" : [
    {
		"city": "Bangalore"
	}
	]
}
```

#### 4. NewField
Useful to add new fields to the target JSON providing default, hardcoded value passed in `args[0]`



## Quick Start

###  Blend's JAR files are available in Github Packages

#### Maven
```
<dependency>
  <groupId>club.theblend</groupId>
  <artifactId>blend</artifactId>
  <version>1.0.0</version>
</dependency>
```

#### Gradle
```
implementation 'club.theblend:blend:1.0.0'  
```

#### Load the YAML config

`Blend blend = new Blend("path-to-yaml-file")`

#### Convert from source to target in just one line of code!
`Target target = blend.convert(sourceJSON, Target.class)`

## Contributing

Contributions for extending transforms are highly appreciated. Refer CONTRIBUTING.md




