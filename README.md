# Learn AutoGen

## Class Relationship

look full graph: [autogen_class_refs.pdf](./autogen_class_refs.pdf)

![](./autogen_class_show.png)

## Basic Definition
```python
# TODO
```
## Tools Definition
```python
function_map not effect, you must use this way , like this:
'''python
@user_proxy.register_for_execution()
@chatbot.register_for_llm(description="Calculate distance between two addresses.")
@observe()
def calculate_distance(
    start_address: Annotated[str, "Starting address"],
    end_address: Annotated[str, "Destination address"],
) -> str:
      .... function detail ....
'''
```
## ReAct Definition
```python
# TODO
```
